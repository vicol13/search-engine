### Corpus search engine

This repository represent an implementation of a small search engine for a corpus of files. Main idea is to take each word which make sense
(*i.e. money, britain, etc. resource/nlp/stop-words will be ignored*) get the base form of it using OpenNlp library (*i.e. person is same word as people*), then using inverted index
we save the word and metadata about (*in which document at which line and at what indexes we can find the word*). 
Also based on the indexed word is built a trie structure which suggest the words which we can use on for searching, second option
is to used weighted trie which sort the results based on how often are they used in corpus.

As corpus are used category business, entertainment and sport [bbc dataset](http://mlg.ucd.ie/datasets/bbc.html).

Main endpoints are 
1. for searching in corpus  
   ```shell
    curl --location --request GET 'http://localhost:8080/search/<your_word_here>'
    ```
2. for getting words based on prefix
    ```shell
   curl --location --request GET 'http://localhost:8080/search?prefix=<your_prefix_here>'
   ```
   as default is used weighted trie, you can change it in ```TrieBeanConfiguration```