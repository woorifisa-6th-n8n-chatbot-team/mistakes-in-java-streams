package step02;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class LazyCase2 {

    static void heavyOperation(int i) {
        try {
            Thread.sleep(5); // 비용 있는 연산 가정
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        List<Integer> list = IntStream.rangeClosed(1, 100)
                                      .boxed()
                                      .toList();

        // Case A: 전체 실행
        AtomicInteger counterA = new AtomicInteger();
        long startA = System.nanoTime();

        for (int i : list) {
            counterA.incrementAndGet();
            heavyOperation(i);
            if (i > 10) {
                // 조건은 있지만 break 없음
            }
        }

        long timeA = System.nanoTime() - startA;

        // Case B: Stream + lazy + short-circuit
        AtomicInteger counterB = new AtomicInteger();
        long startB = System.nanoTime();

        list.stream()
            .filter(i -> {
                counterB.incrementAndGet();
                heavyOperation(i);
                return i > 10;
            })
            .findFirst(); // 단락 연산

        long timeB = System.nanoTime() - startB;

        System.out.println("[Case A] 실행 횟수: " + counterA.get());
        System.out.println("[Case A] 실행 시간(ns): " + timeA);

        System.out.println("[Case B] 실행 횟수: " + counterB.get());
        System.out.println("[Case B] 실행 시간(ns): " + timeB);
    }
}

