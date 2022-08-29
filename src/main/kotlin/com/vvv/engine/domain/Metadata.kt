package com.vvv.engine.domain

import java.util.*


/**
 * This class represent container for data related to file
 * each file name has an associated LineMap in which
 * is contained data about occurrences of a word within file
 *
 *
 * as a chain this classes represent next structure of map
 *
 * { <word:String>:  {
 *      <file_1:String> : {
 *              <line_1:Int>:[Index(1,10),Index(20,25)],
 *              <line_2:Int>:[Index(5,15)]:
 *          }:Map<String,LineMap>
 *      }:Map<String,LineMap>
 * }
 */
class FileMap {

    private val map: MutableMap<String, LineMap> = mutableMapOf()

    fun update(fileName: String): LineMap {
        this.map.putIfAbsent(fileName, LineMap())
        return this.map[fileName]!!
    }

    fun entries(): MutableSet<MutableMap.MutableEntry<String, LineMap>> {
        return this.map.entries
    }

}

/**
 * Class which act as container for holding information related
 * where a word is contained within a file here we have as
 * key number of line where we have word in file and a list of
 * Indexes which represent at which index word is in file
 */
class LineMap {
    private val map: TreeMap<Int, MutableList<Index>> = TreeMap()

    fun update(line: Int, index: Index) {
        this.map.putIfAbsent(line, mutableListOf())
        this.map[line]!!.add(index)
    }

    fun update(line: Int, indexes: Iterable<Index>) {
        indexes.forEach { update(line, it) }
    }

    fun get(line: Int) = this.map[line]

    fun keys() = this.map.keys
}


