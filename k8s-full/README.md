📌 Deployment Guide (Kubernetes)
## 🚀 Kubernetes Deployment Guide

본 프로젝트는 Docker Desktop Kubernetes 환경에서 실행됩니다.

### 1️⃣ Docker Desktop 설정
- Kubernetes Enable 필수
- 리소스 권장: CPU 4 / RAM 8GB 이상

### 2️⃣ 배포 스크립트 실행

```bash
cd k8s-full
./deploy.sh


→ 아래 리소스가 자동 적용됩니다:

Namespace: chat-app

PostgreSQL / Redis

Flyway DB Migration

App Backend Services 5종

NGINX Reverse Proxy

Horizontal Pod Autoscaler(chat-ws)

3️⃣ 서비스 접근
curl http://localhost:30080/auth/health

4️⃣ HPA 상태 확인
kubectl get hpa -n chat-app
kubectl get pods -n chat-app -w


---

### 📊 Monitoring (Prometheus + Grafana)

```md
## 📊 Observability Setup (Prometheus & Grafana)

###
1️⃣ 설치

```bash
kubectl create namespace monitoring
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
helm install k8s-monitoring prometheus-community/kube-prometheus-stack -n monitoring

2️⃣ Grafana 접속
$POD_NAME = kubectl get pods -n monitoring -l "app.kubernetes.io/name=grafana" -o jsonpath="{.items[0].metadata.name}"
kubectl port-forward -n monitoring $POD_NAME 30000:3000


접속 URL: http://localhost:30000

기본 로그인:

ID: admin

PW: Secret decoding 필요

kubectl get secret k8s-monitoring-grafana -n monitoring -o jsonpath="{.data.admin-password}" | % { [System.Text.Encoding]::UTF8.GetString([System.Convert]::FromBase64String($_)) }


---

### ⚙️ 부하 테스트

```md
## ⚙️ HPA 부하 테스트

```bash
kubectl run load-generator --image=busybox -n chat-app -- /bin/sh -c "while true; do wget -q -O- http://chat-ws.chat-app.svc.cluster.local:8080; done"


Pod 수 증가 확인:

kubectl get hpa -n chat-app -w


중단:

kubectl delete pod load-generator -n chat-app
