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

    private int doSearch(final CharBuffer buffer, final int matchedLength,
        final int currentLength)
    {
        final int nextLength = fullWord ? currentLength : matchedLength;

        if (!buffer.hasRemaining())
            return nextLength;

        final int index = Arrays.binarySearch(nextChars, buffer.get());

        return index < 0
            ? nextLength
            : nextNodes[index].doSearch(buffer, nextLength, currentLength + 1);
    }
}
