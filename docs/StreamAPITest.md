# 1. Not Using Terminal Operations:Mistake: Forgetting to call a terminal operation like collect(), forEach(), or reduce(), this leads to no execution.

## 실행 결과 및 결론

![StreamAPITest](StreamAPITest.png)

- 위 결과를 보면 Stream을 사용할 때 중간 연산자는 종단 연산자를 호출하는 시점에 실행된다.

## 이론

- 코드를 구현하면서 종단 연산자를 실행했을 때만 중간 연산자들이 실행되는 이유를 분석한다.

- List 구현체는 아래와 같이 SequencedCollection을 상속
  ![List](./List.png)
- SequencedCollection은 Collection 상속
  ![Collection](./SequencedCollection.png)
- Collection은 Iterable 상속
  ![Collection](./Collection.png)

>  List는 Iterable을 구현한 반복 가능한 객체이다.

- Stream은 BaseStream 상속
- ![Stream](./Stream.png)
- BaseStream은 Iterator(Iterable을 가지고 있는 객체이면서 순회할 수 있음)를 필드로 가지고 있다.
- ![BaseStream](./BaseStream.png)

```java

import java.util.stream.Stream;


public class Main {

    static class CustomIterator<T> {
        private final T[] data;
        private int index = 0;

        CustomIterator(T[] data) {
            this.data = data;
        }

        public boolean hasNext() {
            return index < data.length;
        }

        public T next() {
            return data[index++];
        }
    }

    @FunctionalInterface
    interface CustomTransformer<T, R> {
        R transform(T input);
    }

    static class CustomStream<T> {
        private final CustomIterator<T> iter;

        CustomStream(CustomIterator<T> iter) {
            this.iter = iter;
        }

        // 중간 연산
        public <R> CustomStream<R> map(CustomTransformer<T, R> transformer) {
            CustomIterator<R> mappedIter = new CustomIterator<>(null) {

                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public R next() {
                    T value = iter.next();
                    return transformer.transform(value);
                }
            };

            return new CustomStream<>(mappedIter);
        }

        public long count() {
            long cnt = 0;
            while (iter.hasNext()) {
                iter.next();
                cnt++;
            }
            return cnt;
        }
    }

    static class CustomStream<T> {
        private final CustomIterator<T> iter;

        CustomStream(CustomIterator<T> iter) {
            this.iter = iter;
        }

        // 중간 연산
        public <R> CustomStream<R> map(CustomTransformer<T, R> transformer) {
            CustomIterator<R> mappedIter = new CustomIterator<>(null) {

                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public R next() {
                    T value = iter.next();
                    return transformer.transform(value);
                }
            };

            return new CustomStream<>(mappedIter);
        }

        public long count() {
            long cnt = 0;
            while (iter.hasNext()) {
                iter.next();
                cnt++;
            }
            return cnt;
        }
        
        public void forEach(Consumer<T> consumer) {
            while (iter.hasNext()) {
                T value = (T) iter.next();
                consumer.accept(value);
            }
        }
    }

    static class CustomList<T> {
        private final CustomIterator<T> iterator;

        private CustomList(CustomIterator<T> iterator) {
            this.iterator = iterator;
        }

        public static <T> CustomList<T> asList(T ... data) {
            CustomIterator<T> iterator = new CustomIterator<>(data);
            return new CustomList<>(iterator);
        }

        public CustomStream<T> stream() {
            return new CustomStream<>(this.iterator);
        }
    }

    public static void main(String[] args) {
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        Stream<Integer> javaStream = data.stream()
                .map(num -> {
                    System.out.println("Lambda executed for: " + num);
                    return num * 2;
                });
        System.out.println("Java Stream: Before terminal operation");
        javaStream.forEach(System.out::println);

        CustomList<Integer> customData = CustomList.asList(1, 2, 3, 4, 5);
        CustomStream<Integer> stream = customData.stream()
                .map(num -> {
                    System.out.println("Lambda executed for: " + num);
                    return num * 2;
                });

        System.out.println("CustomStream: Before terminal operation");
        stream.forEach(System.out::println);
    }
}
```

### 결론

- 종단 연산자를 호출하는 경우에만 실제 이터레이터를 순회하면서 값에 중간 연산자를 적용하기 때문에 스트림을 쓸 때에는 종단 연산자를 꼭 사용하자. 
