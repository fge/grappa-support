package com.github.parboiled1.grappa.trie;

import java.nio.CharBuffer;
import java.util.Arrays;

public final class TrieNode
{
    private final boolean fullWord;

    private final char[] nextChars;
    private final TrieNode[] nextNodes;

    TrieNode(final boolean fullWord,
        final char[] nextChars, final TrieNode[] nextNodes)
    {
        this.fullWord = fullWord;
        this.nextChars = nextChars;
        this.nextNodes = nextNodes;
    }

    public int search(final String needle)
    {
        return doSearch(CharBuffer.wrap(needle), fullWord ? 0 : -1, 0);
    }

    private int doSearch(final CharBuffer buffer, int lastDepth,
        final int depth)
    {
        if (fullWord)
            lastDepth = depth;

        if (!buffer.hasRemaining())
            return lastDepth;

        final char c = buffer.get();
        final int index = Arrays.binarySearch(nextChars, c);

        if (index < 0)
            return lastDepth;

        return nextNodes[index].doSearch(buffer, lastDepth, depth + 1);
    }
}
