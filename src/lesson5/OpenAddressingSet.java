package lesson5;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class OpenAddressingSet<T> extends AbstractSet<T> {

    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private int size = 0;

    private final Object deleted = new Object();

    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;
        storage = new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int index = startingIndex(o);
        int start = index;
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return true;
            }
            index = (index + 1) % capacity;
            if (index == start) {
                return false;
            }
            current = storage[index];
        }
        return false;
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null && current != deleted) {
            if (current.equals(t)) {
                return false;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        int ind = startingIndex(o);
        int start = ind;
        Object current = storage[ind];
        while (current != null) {
            if (current.equals(o)) {
                storage[ind] = deleted;
                size--;
                return true;
            }
            ind = (ind + 1) % capacity;
            if (ind == start) {
                return false;
            }
            current = storage[ind];
        }
        return false;
    }
    // Трудоёмкость - O(1/(1-A)), где A = size/capacity.
    // Ресурсоёмкость - O(1)

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new OpenAddressingSetIterator();
    }

    public class OpenAddressingSetIterator implements Iterator<T> {
        int sizeIterator = size();
        int indexIterator = 0;   // Индекс итератора по всей таблице (с учётом null)
        int indexRealElement = 0; // Индекс (номер) элемента в таблице (элемент, не равный null)
        Object currentValue;

        @Override
        public boolean hasNext() {
            if (sizeIterator == 0) {
                return false;
            }
            return indexRealElement != sizeIterator;
        }
        // Трудоёмкость - O(1)
        // Ресурсоёмкость - O(1)

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            do {
                currentValue = storage[indexIterator];
                indexIterator++;
            } while (currentValue == null || currentValue == deleted);
            indexRealElement++;
            return (T) currentValue;
        }
        // Трудоёмкость - O(n)
        // Ресурсоёмкость - O(1)

        @Override
        public void remove() {
            if (currentValue == null) {
                throw new IllegalStateException();
            }
            storage[indexIterator - 1] = deleted;
            size--;
            currentValue = null;
        }
        // Трудоёмкость - O(1)
        // Ресурсоёмкость - O(1)
    }
}
