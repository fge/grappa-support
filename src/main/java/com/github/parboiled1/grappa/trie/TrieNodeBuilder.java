package com.github.parboiled1.grappa.trie;

import java.nio.CharBuffer;
import java.util.Map;
import java.util.TreeMap;

public final class TrieNodeBuilder
{
    private boolean fullWord = false;

    private Map<Character, TrieNodeBuilder> subnodes
        = new TreeMap<>();

    public TrieNodeBuilder addWord(final String word)
    {
        doAddWord(CharBuffer.wrap(word));
        return this;
    }

    private void doAddWord(final CharBuffer buffer)
    {
        if (!buffer.hasRemaining()) {
            fullWord = true;
            return;
        }

        // Otherwise we need to continue; in any event we don't have a full
        // match at this point
        fullWord = false;
        final char c = buffer.get();
        TrieNodeBuilder builder = subnodes.get(c);
        if (builder == null) {
            builder = new TrieNodeBuilder();
            subnodes.put(c, builder);
        }
        builder.doAddWord(buffer);
    }

    public TrieNode build()
    {
        final char[] nextChars = new char[subnodes.size()];
        final TrieNode[] nextNodes = new TrieNode[subnodes.size()];

        int index = 0;
        for (final Map.Entry<Character, TrieNodeBuilder> entry:
            subnodes.entrySet()) {
            nextChars[index] = entry.getKey();
            nextNodes[index] = entry.getValue().build();
            index++;
        }
        return new TrieNode(fullWord, nextChars, nextNodes);
    }
}
