package com.vvv.engine.utils


import java.util.TreeMap
import kotlin.RuntimeException

class TrieException(msg: String) : RuntimeException(msg)


/**
 * Class which represent trie structure for input recommendation
 *
 * Beside child node each tree has a property word, which mean
 * this tree is a leaf(represent a word) we keep this in order
 * to avoid making reference to parent node so having next case
 *
 * t
 * ├── to (is marked as word if is called trie.add(to))
 *     ├── toy
 *     ├── top
 */
open class Trie {
    protected constructor() {

    }

    constructor(words: Set<String>) : this() {
        words.forEach { this.addWord(it) }
    }

    protected val map: TreeMap<String, Trie> = TreeMap<String, Trie>()
    protected var word: String? = null

    private fun isLeaf(): Boolean {
        return word != null
    }

    /**
     * should add word into tree
     * @param word which will be added into tree
     */
    fun addWord(word: String) {
        if (word.isEmpty() || word.isBlank()) {
            throw TrieException("add blank or empty word to trie")
        }
        return this.addWord(word, 1)
    }

    /**
     *  function mark node as leaf node like this
     *  can also have child nodes with words
     *
     *  which means that key/word of this node has an actual meaning
     *  like we
     */
    private fun addLeafNode(word: String) {
        this.map.putIfAbsent(word, Trie())
        this.map[word]!!.word = word

    }

    /**
     * recursively get elements of a word and add them in the trie
     * saying that we want to add "toys" in the tree it will grow it as follows
     * ├── t                        add("toys",1)
     *     ├── to                   add("toys",2)
     *          ├── toy             add("toys",3)
     *               ├── toys       add("toys",4)
     *
     * in the last call toys-node will be marked leaf
     */
    private fun addWord(word: String, index: Int) {
        if (index == word.length) {
//            println(word)
            this.addLeafNode(word)

            return //exit recursion
        }
        val currentSubSequence = word.subSequence(0, index).toString()
        if (!map.containsKey(currentSubSequence)) {
            this.map[currentSubSequence] = Trie()
        }

        this.map[currentSubSequence]!!.addWord(word, index + 1)
    }


    open fun search(prefix: String): List<String> {
        if (prefix.isEmpty() || prefix.isBlank()) {
            throw TrieException("search input can't be empty or blank")
        }
        return search(prefix, 1)
    }

    /**
     * recursively parse the tree in order to find word with root @param prefix
     * assuming that we want to search for prefix "toy" the function chain looks
     * like this root.get("t").get("to").get("toy").getAllChild()
     */
    private fun search(prefix: String, index: Int): List<String> {

        if (!isLeaf() && this.map.isEmpty()) {
//            throw TrieException("Unknown sequence [$prefix]")
            return emptyList()
        }

        if (index == prefix.length + 1) {
            return this.getWords()
        }

        val currentSubSequence = prefix.subSequence(0, index)
        if (map.containsKey(currentSubSequence)) {
            return map[currentSubSequence]!!.search(prefix, index + 1)
        }

//        throw TrieException("Unknown sequence [$prefix]")
        return emptyList()
    }

    /**
     * load all leaf nodes which are kids of this node
     */
    private fun getWords(): List<String> {
        val collectingList = mutableListOf<String>()
        if (this.isLeaf()) {
            collectingList.add(this.word!!)
        }
        collectFromChild(collectingList)

        return collectingList
    }

    private fun collectFromChild(collectingList: MutableList<String>) {
        this.map.values.forEach {
            if (it.isLeaf()) {
                collectingList.add(it.word!!)
            } else if (it.map.isNotEmpty()) {
                it.collectFromChild(collectingList)
            }

        }
    }

    fun print(indent: String = " ") {
        println("$indent keys ${this.map.keys} word [${this.word}] \t\t level:{${indent.length}}")
        if (map.isEmpty()) return
        this.map.forEach {
            it.value.print("$indent ")
        }
    }
}