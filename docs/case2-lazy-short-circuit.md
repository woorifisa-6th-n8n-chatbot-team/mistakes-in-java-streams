# Case 2. Lazy Evaluation과 Short-Circuit로 불필요한 연산은 줄어드는가?

## 📌 Experiment Purpose
Java Stream의 Lazy Evaluation과  
Short-Circuit 연산(`findFirst`)이 **불필요한 연산을 실제로 줄이는지** 검증한다.

---

## 🧪 Experiment Design
- 입력 데이터: 1 ~ 100 정수 리스트
- 동일한 Stream 파이프라인 사용
- 차이점:
  - Case A: 전체 요소 소비 (`toList`)
  - Case B: 단락 연산 (`findFirst`)
- 비교 기준:
  - 실행 횟수
  - 실행 시간

---

## 🧠 Implementation
- `filter()` 내부에서 실행 횟수 카운트
- `Thread.sleep()`을 포함한 연산으로 연산 비용 부여
- `System.nanoTime()`으로 실행 시간 측정

---

## 🔬 Test Cases

### Case A. 전체 소비 (toList)
- 모든 요소에 대해 filter 연산 수행
- Lazy Evaluation 효과 없음

### Case B. Short-Circuit (findFirst)
- 조건 만족 시 즉시 종료
- 이후 요소는 실행되지 않음

---

## 📊 Result

| Case | Execution Count | Execution Time |
|-----|-----------------|----------------|
| Case A (toList) | 100 | 높음 |
| Case B (findFirst) | 11 | 낮음 |

---

## 🔍 Analysis
- `findFirst()`는 조건을 만족하는 순간 Stream 처리를 종료
- Lazy Evaluation과 Short-Circuit이 결합되어
  **불필요한 연산 자체를 제거**
- 실행 횟수 감소 → 실행 시간 감소로 직결됨

---

## ✅ Conclusion
- Lazy Evaluation은 단순히 실행을 지연하는 개념이 아니다
- Short-Circuit 연산과 함께 사용될 경우
  **필요한 연산만 수행하는 최적화 메커니즘**으로 동작한다
- Stream API 사용 시 터미널 연산 선택이 성능에 직접적인 영향을 준다

---

## 📝 Notes
- 성능 비교는 상대적인 경향을 확인하기 위한 목적
- JUnit은 실행 시간 비교에 부적합하여 사용하지 않음
