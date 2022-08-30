### Corpus search engine

This repository represent an implementation of a small search engine for a corpus of files. Main idea is to take each word which make sense
(*i.e. money, britain, etc. resource/nlp/stop-words will be ignored*) get the base form of it using OpenNlp library (*i.e. person is same word as people*), then using inverted index
we save the word and metadata about (*in which document at which line and at what indexes we can find the word*). 
Also based on the indexed word is built a trie structure which suggest the words which we can use  for searching, second option
is to used weighted trie which sort the results based on how often are they used in corpus.

As corpus are used categories business, entertainment and sport [bbc dataset](http://mlg.ucd.ie/datasets/bbc.html).

Main endpoints are 
1. for searching in corpus  
   ```shell
    curl --location --request GET 'http://localhost:8080/search/<your_word_here>'
    ```
   returns : 
   ```json
    "word": "person",
    "files": [
        {
            "fileName": "UK youth interested in politics  -[politics].txt",
            "sentences": [
                {
                    "sentence": "The majority of young people are interested in politics, holding \"strong opinions\" on policies and have a \"keen appetite\" for direct action.",
                    "line": 0,
                    "indexes": [
                        {
                            "begin": 18,
                            "end": 24
                        }
                    ]
                }
        ....
   ```   


2. for getting words based on prefix
    ```shell
   curl --location --request GET 'http://localhost:8080/search?prefix=<your_prefix_here>'
   ```
   as default is used weighted trie, you can change it in ```TrieBeanConfiguration```, return
   
   ```json
   [
   "play",
   "player",
   "playing",
   "playwright",
   "playground",
   "playschool",
   "playboyz",
   "playmaker"
   ]
   ```