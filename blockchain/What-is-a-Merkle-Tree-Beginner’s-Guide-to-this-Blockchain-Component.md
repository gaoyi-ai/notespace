---
title: What is a Merkle Tree? Beginner’s Guide to this Blockchain Component
categories:
- blockchain
- Merkle Tree
tags:
- Merkle Tree
date: 2021/6/27
---



> [What is a Merkle Tree? Beginner's Guide to this Blockchain Component (blockonomi.com)](https://blockonomi.com/merkle-tree/)

# What is a Merkle Tree? Beginner’s Guide to this Blockchain Component

> Merkle trees are an integral component of blockchains and effectively allow them to function with provable immutability and transaction integrity.

Merkle Trees are a fundamental component of blockchains that underpin their functionality. They allow for efficient and secure verification of large data structures, and in the case of blockchains, potentially boundless data sets.

The implementation of Merkle trees in blockchains has multiple effects. It allows them to scale while also providing the hash-based architecture for them to maintain data integrity and a trivial way to verify the integrity of data.

Cryptographic hash functions are the underlying technology that allow for Merkle trees to work, so first, it is important to understand what cryptographic hash functions are.

## Cryptographic Hash Functions

Contents [[Show](https://blockonomi.com/merkle-tree/#)]

Simply put, a hash function is any function that is used to map data of an arbitrary size (input) to a fixed size output. A hashing algorithm is applied to the data input and the resulting fixed length output is referred to as the hash.

Many hashing algorithms are widely publicly available and can be selected based on your needs.

The resulting hash from the arbitrary input is not only fixed in length, it is also completely unique to the input and the function itself is deterministic. That is, no matter how many times you run the function on the same input, the output will always be the same.

For instance, if you have the following data sets below as an input, the resulting outputs are unique for each input. Notice how in the second and third examples, even though the difference of the inputs is only one word, the resulting outputs are completely different.

This is very important as it allows for “fingerprinting” of data.

![img](https://blockonomi-9fcd.kxcdn.com/wp-content/uploads/2018/06/hash.jpg)

A cryptographic hash function, Image from [Wikipedia](https://commons.wikimedia.org/wiki/File:Hash_function.svg)

Since the output (hash sum in the example) length is always the same as determined by the hashing algorithm used, huge amounts of data can be identified solely through their resulting hash.

With systems that contain massive amounts of data, the benefits of being able to store and identify data with a fixed length output can create vast storage savings and help to increase efficiency.

Within blockchains, hashing algorithms are used to determine the state of the blockchain.

Blockchains are linked lists that contain data and a hash pointer that points to the previous block, creating a chain of connected blocks, hence the name “blockchain”.

Each block is connected to each other through a hash pointer, which is the hash of the data inside the previous block along with the address of the previous block. By linking blocks of data in this format, each resulting hash of the previous block represents the entire state of the blockchain since all of the hashed data of the previous blocks is hashed into one hash.

This is represented (in the case of the SHA-256 algorithm) by an output (hash) such as this.

 

```
b09a57d476ea01c7f91756adff1d560e579057ac99a28d3f30e259b30ecc9dc7
```

The hash above is the fingerprint of the entire state of the blockchain before it. The state of the blockchain prior to the new block (as hashed data) is the input, and the resulting hash is the output.

Although it is possible to use cryptographic hashes without Merkle trees, it is extremely inefficient and not scalable. Using hashes to store data in a block in a series format is time-consuming and cumbersome.

As you will see, Merkle trees allow for trivial resolution of data integrity as well as mapping of that data through the entire tree using Merkle proofs.

## Merkle Trees and Merkle Proofs

Named after Ralph Merkle, who patented the concept in 1979, Merkle trees fundamentally are data structure trees where each non-leaf node is a hash of its respective child nodes.

The leaf nodes are the lowest tier of nodes in the tree. At first, it may sound difficult to comprehend, but if you look at the commonly used figure below, it will become much easier to understand.

![Hash Tree](https://blockonomi-9fcd.kxcdn.com/wp-content/uploads/2018/06/hash-tree.jpg)

An example of a binary hash tree, Image from [Wikipedia](https://en.wikipedia.org/wiki/Merkle_tree)

Importantly, notice how the non-leaf nodes or “branches” (represented by Hash 0-0 and Hash 0-1) on the left side, are hashes of their respective children L1 and L2. Further, notice how branch Hash 0 is the hash of its concatenated children, branches Hash 0-0 and Hash 0-1.

The example above is the most common and simple form of a Merkle tree known as a Binary Merkle Tree. As you can see, there is a top hash that is the hash of the entire tree, known as the root hash. Essentially, Merkle trees are a data structure that can take “n” number of hashes and represent it with a single hash.

The structure of the tree allows for efficient mapping of arbitrarily large amounts of data and enables easy identification of where changes in that data occur. This concept enables Merkle proofs, with which, someone can verify that the hashing of data is consistent all the way up the tree and in the correct position without having to actually look at the entire set of hashes.

Instead, they can verify that a data chunk is consistent with the root hash by only checking a small subset of the hashes rather than the entire data set.

As long as the root hash is publicly known and trusted, it is possible for anyone who wants to do a key-value lookup on a database to use a Merkle proof to verify the position and integrity of a piece of data within a database that has a particular root.

When the root hash is available, the hash tree can be received from any non-trusted source and one branch of the tree can be downloaded at a time with immediate verification of data integrity, even if the whole tree is not yet available.

One of the most important benefits of the Merkle tree structure is the ability to authenticate arbitrarily large sets of data through a similar hashing mechanism that is used to verify much smaller amounts of data.

The tree is advantageous for distributing large sets of data into manageable smaller parts where the barrier for the verification of integrity is substantially reduced despite the overall larger data size.

The root hash can be used as the fingerprint for an entire data set, including an entire database or representing the entire state of a blockchain. In the following sections, we will discuss how [Bitcoin](https://blockonomi.com/buy-bitcoin/) and other systems implement Merkle trees.

## Merkle Trees in Bitcoin

The cryptographic hash function employed by [Bitcoin](https://blockonomi.com/bitcoin/) is the SHA-256 algorithm. This stands for “Secure Hashing Algorithm”, whose output is a fixed 256 bits in length. The basic function of Merkle trees in [Bitcoin](https://blockonomi.com/how-to-make-money-bitcoin/) is [to store](https://blockonomi.com/bluehost-coupon-code/), and eventually prune transactions in every block.

As mentioned earlier, blocks in a blockchain are connected through hashes of the previous block. In Bitcoin, each block contains all of the transactions within that block as well as the block header which consists of:

- Block Version Number
- Previous Block Hash
- Timestamp
- Mining Difficulty Target
- Nonce
- Merkle Root Hash

The image below is from the Bitcoin [whitepaper](https://bitcoin.org/bitcoin.pdf) and illustrates how the Merkle tree fits into each block.

![Merkle Tree](https://blockonomi-9fcd.kxcdn.com/wp-content/uploads/2018/06/merkle-tree.jpg)

The transactions are included into blocks by miners and are hashed as part of a Merkle tree, leading to the Merkle root that is stored in the block header. This design has a number of distinct benefits.

Most notably, as outlined in the whitepaper, this allows for existence of Simple Payment Verification (SPV) nodes, also known as “lightweight clients”. These nodes do not have to download the entire Bitcoin blockchain, only the block headers of the longest chain.

SPV nodes can achieve this by querying their peer nodes until they are convinced that the stored block headers they are operating on are part of the longest chain. An SPV node is able to then determine the status of a transaction by using the Merkle proof to map the transaction to a specific Merkle tree with that respective Merkle tree’s root hash in a block header that is part of the longest chain.

Additionally, Bitcoin’s implementation of Merkle trees allows for pruning of the blockchain in order to save space. This is a result of only the root hash being stored in the block header, therefore, old blocks can be pruned by removing unnecessary branches of the Merkle tree while only preserving those needed for the Merkle proof.

## Implementation of Merkle Trees in Other Blockchains and Systems

Although Bitcoin was the first blockchain to implement Merkle trees, many other blockchains implement similar Merkle tree structures or even more complex versions.

Further, Merkle tree implementation is not only limited to blockchains and is applied to a variety of other systems.

[Ethereum](https://blockonomi.com/ethereum-guide/), being the other most recognizable cryptocurrency, is also a great example of a different Merkle tree implementation. Because Ethereum is turing-complete as [a platform](https://blockonomi.com/siteground-coupon-code/) for building much more complex applications, it uses a more complex version of the Merkle tree called a Merkle Patricia Tree that is actually 3 separate Merkle trees used for three kinds of objects. You can learn more about these trees [here](https://easythereentropy.wordpress.com/2014/06/04/understanding-the-ethereum-trie/).

Finally, Merkle trees are important component of distributed version control systems such as Git and [IPFS](https://blockonomi.com/interplanetary-file-system/). Their ability to easily ensure and verify the integrity of data shared between computers in a P2P format makes them invaluable to these systems.

## Conclusion

Merkle trees are an integral component of blockchains and effectively allow them to function with provable immutability and transaction integrity.

Understanding the role that they play in distributed networks and their underlying technology of cryptographic hash functions is crucial to grasping the basic concepts within cryptocurrencies as they continue to develop into larger and more complex systems.