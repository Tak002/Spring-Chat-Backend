#!/bin/bash

set -e

echo "=== chat-app 클러스터 배포 시작 ==="

# Namespace 생성
kubectl apply -f namespace.yaml

# Secret 생성
kubectl apply -f secret-env.yaml

# ConfigMap(SQL) 먼저 적용 (SQL 포함!)
kubectl apply -f flyway-sql-configmap.yaml

# DB & Redis 먼저 적용
kubectl apply -f postgres.yaml
kubectl apply -f redis.yaml

echo "Postgres 준비 대기..."
kubectl wait --for=condition=ready pod -l app=postgres -n chat-app --timeout=300s

# Flyway Migration Job 실행
echo "DB 마이그레이션 진행중..."
kubectl apply -f flyway-job.yaml

# 완료되거나 실패 여부 체크
if kubectl wait --for=condition=complete job/db-migrate -n chat-app --timeout=300s; then
  echo "DB 마이그레이션 완료!"
else
  echo "⚠️ DB 마이그레이션 타임아웃 — 로그를 확인해야 합니다"
fi

# 완료되었으면 Job 삭제 (재배포 대비)
kubectl delete job db-migrate -n chat-app --ignore-not-found=true

# Chat App Backend 배포
kubectl apply -f app-auth.yaml
kubectl apply -f app-media.yaml
kubectl apply -f app-service.yaml
kubectl apply -f chat-ws.yaml
kubectl apply -f chat-history.yaml

# HPA 적용 (chat-ws auto scaling)
kubectl apply -f chat-ws-hpa.yaml

# Reverse Proxy + ConfigMap + Ingress
kubectl apply -f configmap-nginx.yaml
kubectl apply -f reverse-proxy.yaml
kubectl apply -f ingress.yaml

echo "=== 배포 완료! ==="
kubectl get pods -n chat-app
kubectl get svc -n chat-app
kubectl get ingress -n chat-app
