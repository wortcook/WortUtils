package com.wortcook;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import org.junit.jupiter.api.Test;

public class WortTest {
    @Test
    void tryOptional(){
        Optional<String> o = Wort.tryOptional(() -> "A");
        assertTrue(o.isPresent());

        o = Wort.tryOptional(() -> {throw new RuntimeException();});
        assertTrue(!o.isPresent());
    }

    @Test
    void tryDefault(){
        String s = Wort.tryDefault(() -> "A", "B");
        assertEquals("A",s);

        s = Wort.tryDefault(() -> {throw new RuntimeException();}, "B");
        assertEquals("B",s);

        s = Wort.tryDefault((String a) -> a, "A", "B");
        assertEquals("A",s);

        s = Wort.tryDefault((String a) -> {throw new RuntimeException();}, "A", "B");
        assertEquals("B",s);

        s = Wort.tryDefault((String a, String b) -> a + b, "A", "B", "C");
        assertEquals("AB",s);

        s = Wort.tryDefault((String a, String b) -> {throw new RuntimeException();}, "A", "B", "C");
        assertEquals("C",s);
    }

    @Test
    void tryRepeat(){
        String s = Wort.tryRepeat(3, () -> "A");
        assert(s.equals("A"));

        final Map<String, Integer> m = new HashMap<>();
        m.put("failCount",0);
        m.put("exceptionCount",0);

        s = Wort.tryRepeat(3, () -> {
            if(m.get("failCount") < 2){
                m.put("failCount", m.get("failCount") + 1);
                m.put("exceptionCount", m.get("exceptionCount") + 1);
                throw new RuntimeException();
            }
            return "A";
        });
        assert(s.equals("A"));
        assertEquals(m.get("exceptionCount"), 2);

        
        assertThrows(java.lang.RuntimeException.class, () -> Wort.tryRepeat(3, () -> {throw new RuntimeException();}));
    }

    @Test
    void withLock(){
        final ReadLock lock = new ReentrantReadWriteLock().readLock();

        String s = Wort.withLock(lock, () -> "A");
        assertEquals("A",s);

        assertThrows(java.lang.RuntimeException.class, () -> Wort.withLock(lock, () -> {throw new RuntimeException();}));
    }
}
