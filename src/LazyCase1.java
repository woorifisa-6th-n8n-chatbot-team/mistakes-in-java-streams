package step02;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class LazyCase1 {

    static void heavyOperation(int i) {
        try {
            Thread.sleep(5); // 실행 여부를 명확히 보기 위한 지연
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        List<Integer> list = IntStream.rangeClosed(1, 100)
                                      .boxed()
                                      .toList();

        // Case A
        AtomicInteger counterA = new AtomicInteger();
        long startA = System.nanoTime();

        list.stream()
            .map(i -> {
                counterA.incrementAndGet();
                heavyOperation(i);
                return i * 2;
            });

        long timeA = System.nanoTime() - startA;

        // Case B
        AtomicInteger counterB = new AtomicInteger();
        long startB = System.nanoTime();

        list.stream()
            .map(i -> {
                counterB.incrementAndGet();
                heavyOperation(i);
                return i * 2;
            })
            .toList(); // 터미널 연산

        long timeB = System.nanoTime() - startB;

        System.out.println("[Case A] 실행 횟수: " + counterA.get());
        System.out.println("[Case A] 실행 시간(ns): " + timeA);

        System.out.println("[Case B] 실행 횟수: " + counterB.get());
        System.out.println("[Case B] 실행 시간(ns): " + timeB);
    }
}
