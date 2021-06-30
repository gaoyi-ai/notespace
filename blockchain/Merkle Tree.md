---
title: Merkle Tree
categories:
- Blockchain
- Merkle Tree
tags:
- blockchain
- merkle tree
date: 2021/6/30
---



# Merkle Tree

> [Merkle Tree (investopedia.com)](https://www.investopedia.com/terms/m/merkle-tree.asp)

## What Is a Merkle Tree?

A Merkle tree is a data structure that is used in computer science applications. In bitcoin and other cryptocurrencies, Merkle trees serve to encode blockchain data more efficiently and securely.

They are also referred to as "binary hash trees."



## Breaking Down Merkle Tree

In [bitcoin's](https://www.investopedia.com/terms/b/bitcoin.asp) [blockchain](https://www.investopedia.com/terms/b/blockchain.asp), a block of transactions is run through an algorithm to generate a [hash](https://www.investopedia.com/terms/h/hash.asp), which is a string of numbers and letters that can be used to verify that a given set of data is the same as the original set of transactions, but not to obtain the original set of transactions. Bitcoin's software does not run the entire block of transaction data—representing 10 minutes' worth of transactions on average—through the hash function at one time, however.1 Rather, each transaction is hashed, then each pair of transactions is concatenated and hashed together, and so on until there is one hash for the entire block. (If there is an odd number of transactions, one transaction is doubled and its hash is concatenated with itself.)

Visualized, this structure resembles a tree. In the diagram below, "T" designates a transaction, "H" a hash. Note that the image is highly simplified; an average block contains over 500 transactions, not eight.2

![Merkle Tree](https://www.investopedia.com/thmb/a6YFCp_I9oYwCkWnLO-xybzquTk=/6250x3959/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/MerkleTree-5590a1ca4e904b6e8e60b6257751e840.png)

Image by Julie Bang © Investopedia 2020

The hashes on the bottom row are referred to as "leaves," the intermediate hashes as "branches," and the hash at the top as the "root." The Merkle root of a given block is stored in the header: for example, the Merkle root of [block](https://blockexplorer.com/blocks) #482819 is e045b18e7a3d708d686717b4f44db2099aabcad9bebf968de5f7271b458f71c8. The root is combined with other information (the software version, the previous block's hash, the timestamp, the difficulty target, and the nonce) and then run through a hash function to produce the block's unique hash: 000000000000000000bfc767ef8bf28c42cbd4bdbafd9aa1b5c3c33c2b089594 in the case of block #482819. This hash is not actually included in the relevant block, but the next one; it is distinct from the Merkle root.3

The Merkle tree is useful because it allows users to verify a specific transaction without downloading the whole blockchain (over 130 gigabytes at the end of August 2017).4 For example, say that you wanted to verify that transaction TD is included in the block in the diagram above. If you have the root hash (HABCDEFGH), the process is like a game of sudoku: you query the network about HD, and it returns HC, HAB, and HEFGH. The Merkle tree allows you to verify that everything is accounted for with three hashes: given HAB, HC, HEFGH, and the root HABCDEFGH, HD (the only missing hash) has to be present in the data.

![Merkle Tree 2](https://www.investopedia.com/thmb/ELuNL8nogaBdn-qHW2SVEN2xOPo=/6250x3959/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/MerkleTree2-9c2dac8d27184403b532663085c0eb90.png)

Image by Julie Bang © Investopedia 2020

Merkle trees are named after Ralph Merkle, who proposed them in a 1987 paper titled "[A Digital Signature Based on a Conventional Encryption Function](http://people.eecs.berkeley.edu/~raluca/cs261-f15/readings/merkle.pdf)." Merkle also invented cryptographic hashing.5



### ARTICLE SOURCES

Investopedia requires writers to use primary sources to support their work. These include white papers, government data, original reporting, and interviews with industry experts. We also reference original research from other reputable publishers where appropriate. You can learn more about the standards we follow in producing accurate, unbiased content in our [editorial policy.](https://www.investopedia.com/legal-4768893#EditorialPolicy)

1. Bitcoin.org. "[Frequently Asked Questions](https://bitcoin.org/en/faq#general)." Accessed June 25, 2021.
2. YCharts. "[Bitcoin Average Transactions Per Block](https://ycharts.com/indicators/bitcoin_average_transactions_per_block)." Accessed June 25, 2021.
3. Blockchain.com. "[Block 482819](https://www.blockchain.com/btc/block/482819)." Accessed June 25, 2021.
4. YCharts. "[Bitcoin Blockchain Size](https://ycharts.com/indicators/bitcoin_blockchain_size)." Accessed June 25, 2021.
5. Ralph C. Merkle. "[A Digital Signature Based on a Conventional Encryption Function](http://people.eecs.berkeley.edu/~raluca/cs261-f15/readings/merkle.pdf)." Accessed June 25, 2021.