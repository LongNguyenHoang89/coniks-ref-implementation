#CONIKS

Copyright (C) 2015-16 Princeton University.

http://coniks.org

##Introduction
CONIKS is a key management service that provides consistency and privacy for end-user public keys. It protects users against malicious or coerced key servers which may want to impersonate these users to compromise their secure communications: CONIKS will quickly detect any spurious keys, or any versions of the key directory that are inconsistent between two or more users. Nonetheless, CONIKS users do not need to worry about or even see these protocols, or the encryption keys, as CONIKS seamlessly integrates into any existing secure messaging application.

##Extension to CONIKS
We develop an extension to the CONIKS server to do the following things:

* Integrate with IPFS to store CONIKS certificate tree
* Integrate with Ethereum to publish STR in a public log manner