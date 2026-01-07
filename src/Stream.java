package lab02;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Stream {
	
	
	@Test
	public void streamTest(){
		List<String> names = Arrays.asList("alice", "bob","charlie");
		Object name = names.stream().filter(v -> v.startsWith("a"));
		System.out.println(name);
	}
	
	public void streamTest2() {
		List<String> names = Arrays.asList("alice", "bob","charlie");
		names.stream().filter(v -> v.startsWith("a")).forEach(System.out::println);
	}
	
	
	
	private List<String> names;

    @Before
    public void setUp() {
        // 10,000개의 이름 데이터 준비
        names = IntStream.range(0, 10000)
                .mapToObj(i -> (i % 3 == 0 ? "alice" : "bob") + i)
                .collect(Collectors.toList());
    }

    @Test
    public void comparePerformance() {
        // --- 1. 일반 For-loop 측정 ---
        long startFor = System.nanoTime();
        List<String> result1 = new ArrayList<>();
        for (String name : names) {
            if (name.startsWith("alice")) {
                result1.add(name);
            }
        }
        long endFor = System.nanoTime();
        double forTime = (endFor - startFor) / 1_000_000.0;

        // --- 2. 순차 스트림(Sequential Stream) 측정 ---
        long startSeq = System.nanoTime();
        List<String> result2 = names.stream()
                .filter(n -> n.startsWith("alice"))
                .collect(Collectors.toList());
        long endSeq = System.nanoTime();
        double seqTime = (endSeq - startSeq) / 1_000_000.0;

        // --- 3. 병렬 스트림(Parallel Stream) 측정 ---
        long startPar = System.nanoTime();
        List<String> result3 = names.parallelStream()
                .filter(n -> n.startsWith("alice"))
                .collect(Collectors.toList());
        long endPar = System.nanoTime();
        double parTime = (endPar - startPar) / 1_000_000.0;

        // 결과 출력
        System.out.println("========== 성능 분석 결과 ==========");
        System.out.printf("For-loop      : %.4f ms\n", forTime);
        System.out.printf("Sequential    : %.4f ms\n", seqTime);
        System.out.printf("Parallel      : %.4f ms\n", parTime);
        System.out.println("===================================");
    }
}
