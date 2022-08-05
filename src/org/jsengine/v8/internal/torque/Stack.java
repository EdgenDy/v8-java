package org.jsengine.v8.internal.torque;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.function.Consumer;

public class Stack<T> implements Iterable<T> {
    public Vector<T> elements_ = new Vector<T>();

    public Stack() { }

    public Stack(Vector<T> v) {
        elements_ = v;
    }

    public int size() {
        return elements_.size();
    }

    public StackRange topRange(int slot_count) {
        return new StackRange(aboveTop().subtract(slot_count), aboveTop());
    }

    public BottomOffset aboveTop() {
        return new BottomOffset(size());
    }

    public void push(T x) {
        elements_.add(x);
    }

    public StackRange pushMany(Vector<T> v) {
        for (T x : v) {
            push(x);
        }
        return topRange(v.size());
    }

    public void deleteRange(StackRange range) {
        if (range.size() == 0) return;
        for (BottomOffset i = range.end(); i.lessThan(aboveTop()); i.increment()) {
            elements_.set(i.offset - range.size(), elements_.get(i.offset));
        }

        for(int index = elements_.size() - 1; elements_.size() > (elements_.size() - range.size()); index--) {
            elements_.remove(index);
        }
    }

    public T peek(BottomOffset from_bottom) {
        return elements_.get(from_bottom.offset);
    }

    public T pop() {
        T result = elements_.lastElement();
        elements_.removeElementAt(elements_.size() - 1);
        return result;
    }

    public Vector<T> popMany(int count) {
        Vector<T> result = new Vector<T>();
        for(int index = elements_.size() - count, end = elements_.size(); index < end; ++index) {
            result.add(elements_.get(index));
        }
        elements_.setSize(elements_.size() - count);
        return result;
    }

    public Iterator begin() {
        return new Iterator(0, elements_);
    }

    public Iterator end() {
        return new Iterator(elements_.size(), elements_);
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new Iter();
    }

    private class Iter implements java.util.Iterator<T> {
        private int cursor = -1;
        private int limit = 0;

        public Iter() {
            limit = elements_.size();
            if (limit > 0)
                cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return (cursor < (limit - 1));
        }

        @Override
        public T next() {
            if(cursor == -1 || cursor >= limit)
                throw new NoSuchElementException();
            return elements_.get(cursor++);
        }
    }

    public static class Iterator<T> {
        private int index;
        private Vector<T> elements;

        public Iterator(int index, Vector<T> elements) {
            this.index = index;
            this.elements = elements;
        }

        public boolean isGreaterThan(Iterator iterator) {
            return this.index > iterator.index;
        }

        public boolean isLessThan(Iterator iterator) {
            return this.index > iterator.index;
        }

        public boolean isEquals(Iterator iterator) {
            return this.index == iterator.index;
        }

        public boolean notEquals(Iterator iterator) {
            return this.index != iterator.index;
        }

        public T value() {
            return elements.get(index);
        }

        public Iterator<T> increase(int increment) {
            this.index += increment;
            return this;
        }

        public Iterator<T> decrease(int decrement) {
            this.index -= decrement;
            return this;
        }

        public void value(T val) {
            elements.setElementAt(val, index);
        }
    }

    public T top() {
        return peek(aboveTop().subtract(1));
    }

    public void poke(BottomOffset from_bottom, T x) {
        elements_.setElementAt(x, from_bottom.offset);
    }
}
