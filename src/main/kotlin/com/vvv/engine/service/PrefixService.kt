package com.vvv.engine.service


interface PrefixService {
    /**
     *  function take as input some prefix like "to" and it should
     *  return all possible words with this prefix like "toy","toys","top"
     *  if there are no word it should return empty list
     */
    fun search(prefix: String): List<String>
}