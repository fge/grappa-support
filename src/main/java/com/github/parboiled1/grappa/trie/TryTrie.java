package com.github.parboiled1.grappa.trie;

public final class TryTrie
{
    private static final String[] KEYWORDS = {
        "abstract", "assert", "boolean", "break", "byte", "case", "catch",
        "char", "class", "const", "continue", "default", "double", "do",
        "else", "enum", "extends", "finally", "final", "float", "for",
        "goto", "if", "implements", "import", "instanceof", "interface", "int",
        "long", "native", "new", "package", "private", "protected", "public",
        "return", "short", "static", "strictfp", "super", "switch",
        "synchronized", "this", "throws", "throw", "transient", "try", "void",
        "volatile", "while", "false", "null", "true"
    };

    private static final String[] INPUTS = {
        "abstrac", "int", "asser", "while", "strictfp", "for", "if",
        "package", "syncronized", "transient", "volatil", "double", "do",
        "inport", "else", "continua", "finall", "instanceof"
    };

    public static void main(final String... words)
    {
        final TrieNodeBuilder builder = new TrieNodeBuilder();
        for (final String keyword: KEYWORDS)
            builder.addWord(keyword);

        final TrieNode node = builder.build();

        for (final String input : INPUTS)
            System.out.printf("%s: %d\n", input, node.search(input));

        System.exit(0);
    }
}
