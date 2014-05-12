package com.github.parboiled1.grappa.trie;

import javax.annotation.Nonnull;

public final class Trie
{
    private final int maxLength;
    private final TrieNode node;

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public int getMaxLength()
    {
        return maxLength;
    }

    public int search(final String needle)
    {
        return node.search(needle);
    }

    private Trie(final Builder builder)
    {
        maxLength = builder.maxLength;
        node = builder.nodeBuilder.build();
    }

    public static final class Builder
    {
        private int maxLength = 0;
        private final TrieNodeBuilder nodeBuilder
            = new TrieNodeBuilder();

        private Builder()
        {
        }

        public Builder addWord(@Nonnull final String word)
        {
            maxLength = Math.max(maxLength, word.length());
            nodeBuilder.addWord(word);
            return this;
        }

        public Trie build()
        {
            return new Trie(this);
        }
    }
}
