# Case 1. 터미널 연산이 없는 Stream은 실행되지 않는가?

## 📌 Experiment Purpose
Java Stream은 Lazy Evaluation 기반으로 동작한다.  
본 실험에서는 **터미널 연산이 없는 경우 Stream 파이프라인이 실제로 실행되지 않는지**를 검증한다.

---

## 🧪 Experiment Design
- 입력 데이터: 1 ~ 100 정수 리스트
- 중간 연산: `map()`
- 터미널 연산 유무에 따른 실행 여부 비교
- 실행 여부 판단 기준:
  - 연산 실행 횟수
  - 실행 시간

---

## 🧠 Implementation
- `AtomicInteger`를 사용해 map 연산 실행 횟수 카운트
- `System.nanoTime()`으로 실행 시간 측정
- `Thread.sleep()`을 포함한 연산을 통해 실행 여부를 명확히 확인

---

## 🔬 Test Cases

### Case A. 터미널 연산 없음
- `map()`만 정의
- Stream 파이프라인 실행되지 않음

### Case B. 터미널 연산 존재
- `toList()` 호출
- Stream 파이프라인 정상 실행

---

## 📊 Result

| Case | Execution Count | Execution Time |
|-----|-----------------|----------------|
| Case A (No Terminal) | 0 | ~0 |
| Case B (With Terminal) | 100 | 유의미 |

---

## ✅ Conclusion
- Stream은 **중간 연산만으로는 실행되지 않는다**
- 터미널 연산이 호출되는 시점에 파이프라인 전체가 실행된다
- Java Stream의 Lazy Evaluation 특성이 실제 실행 흐름에서 확인되었다

---

## 📝 Notes
- 본 실험은 성능 비교가 아닌 **실행 여부 검증**을 목적으로 한다
- 실행 시간은 절대값보다 **실행 유무 확인용 지표**로 사용한다
