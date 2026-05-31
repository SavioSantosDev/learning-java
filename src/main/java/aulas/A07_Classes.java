package aulas;


class MyList<T> {
    private T[] elements;
    private int size;

    public MyList(T[] elements) {
        this.elements = elements;
        this.size = elements.length;
    }

    public Iterator iterator() {
        return new Iterator();
    }

    class Iterator implements java.util.Iterator<T> {
        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public T next() {
            return elements[cursor++];
        }
    }
}

public class A07_Classes {

    static void main() {
        MyList<String> list = new MyList(new String[]{"a", "b", "c"});
        MyList<String>.Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

}
