// connect-only-handshake.js
import ws from 'k6/ws';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        connect_only: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                // 필요에 맞게 조정: 목표 동시 연결 수
                { duration: '1m', target: 1000 }, // 2분 동안 0 -> 1000 VUs
                // { duration: '5m', target: 1000 }, // 5분 유지 (동시 연결 유지)
                { duration: '20s', target: 0 },    // 정리
            ],
            gracefulStop: '30s',
        },
    },
    thresholds: {
        'checks{phase:handshake}': ['rate>=0.999'], // 99.9% 이상 101 성공
    },
};

// 빌드/배포가 __WS_ENDPOINT__를 실제 값으로 치환했다면 그 값을 사용.
// 치환이 안 되면 문자열 그대로 남으니, 그때는 WS_ENDPOINT env → localhost 순서로 폴백.
function resolveEndpoint() {
    const injected = "__WS_ENDPOINT__";
    if (injected && injected !== "__WS_ENDPOINT__") return injected;
    if (__ENV.WS_ENDPOINT && __ENV.WS_ENDPOINT.trim() !== '') return __ENV.WS_ENDPOINT.trim();
    return 'ws://localhost:8080/ws';
}

const ENDPOINT = resolveEndpoint();

// 각 가상 유저는 연결만 하고 지정 시간 유지 -> 동시 연결 수를 형성
export default function () {
    const holdMs = Number(__ENV.HOLD_MS || 60_000); // 연결 유지 시간(ms), 기본 60초

    const res = ws.connect(ENDPOINT, {}, (socket) => {
        socket.on('open', () => {
            // 핸드셰이크 완료 후 아무 것도 안 함 (순수 연결 유지)
        });

        socket.on('error', (e) => {
            // 에러 로깅 (원하면 주석 해제)
            // console.error('ws error:', e);
        });

        // 지정 시간 유지 후 종료
        socket.setTimeout(() => socket.close(), holdMs);
    });

    check(res, { 'status 101': (r) => r && r.status === 101 }, { phase: 'handshake' });
    // 짧은 sleep은 VU 루프를 급하게 재실행하지 않도록 완충
    sleep(1);
}
