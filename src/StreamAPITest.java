package step02;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class StreamAPITest {

	public static void main(String[] args) {
	}

	private List<String> generateLargeList(int size) {
		List<String> list = new ArrayList();
		for (int i = 0; i < size; i++) {
			list.add("Name" + i);
		}
		return list;
	}

	@Test
	public void testStreamWithoutTerminalOperation() {
		List<String> names = generateLargeList(10_000_000);

		long start = System.currentTimeMillis();

		// 중간 연산만 -> 실행 안 됨
		names.stream().filter(name -> name.startsWith("Name1")).map(String::toUpperCase);

		long end = System.currentTimeMillis();
		long duration = end - start;

		System.out.println("Stream without terminal operation: " + duration + " ms");

		// 거의 즉시 끝나야 함
		assertTrue("Stream without terminal operation should be very fast", duration < 100);
	}

	@Test
	public void testStreamWithTerminalOperation() {
		List<String> names = generateLargeList(10_000_000);

		long start = System.currentTimeMillis();

		// terminal operation 포함 -> 실제 실행
		long count = names.stream().filter(name -> name.startsWith("Name1")).map(String::toUpperCase).count();

		long end = System.currentTimeMillis();
		long duration = end - start;

		System.out.println("Stream with terminal operation: " + duration + " ms");
		System.out.println("Count: " + count);

		// 실제 실행이므로 시간이 오래 걸릴 수 있음
		assertTrue("Stream with terminal operation should take noticeable time", duration > 100);
		assertTrue("Count should be greater than zero", count > 0);
	}
}
