#CONIKS Test Client

Copyright (C) 2015-16 Princeton University.

http://coniks.org

##Introduction
This is a simple test client for the CONIKS key management system. It supports new key registrations, key lookups, key changes (**new feature**), and user policy changes (e.g. key change policy) (**new feature**). It is designed to communicate with the basic implementation of a [CONIKS server](https://github.com/coniks-sys/coniks-ref-implementation/tree/master/coniks_server).

##Building the Test Client
- Prerequisites:
You'll need Java JDK7 or greater and need to ensure that the Google protobufs have been compiled with the most recent version of protoc. If you've already compiled the protobufs for the server, you don't need to
repeat this step for the client.
You'll also need to install the automake and build-essential packages.
- Compiling:
The default classpath is *bin*. If you'd like to change this, you'll need to change the ```CLASS_DEST``` 
variable in the Makefile. To build, run:
```
make 
```

##Using the Test Client

The CONIKS test client has two operating modes: Test Mode and Full Operation. 
Running the client in test mode allows you to still test all CONIKS protocols and operations,
but requires less setup as you can simply use the default configuration in the included *config* file.
**Note:** You must be running the server in the same operating mode.

### Setup
- Full operation mode only: Import the server certificate(s) for SSL/TLS communication.
Repeat the following steps for each server certificate:
```
keytool -import -alias <alias> -file <certificate file> -keystore <truststore name>
```
The alias must match the alias used when generating the cert for the server. You will be asked to
enter a password for each truststore. Make sure to remember this password.
- Set all of the configurations in the config file:
Defaults for the port number and user keys directory are already set, 
except for the absolute path to the keystore generated in the 
previous step along with its password. You'll have to set these using the format
described below.
You may write your own config file, but it must follow the following format:
```
<port number> (must be the same in the CONIKS server config)
<user keys dir>
```
- Set all of the configs in the run script *coniks_test_client.sh*:
Defaults are already set, but you may change the following variables:
```CLASS_DEST``` if you used a different classpath when building the client.
```CONIKS_CLIENTCONFIG``` if you're using a different config file
```CONIKS_CLIENTLOGS``` to store the client logs somewhere other than a *logs* directory

###Running
We provide a run script for the CONIKS test client *coniks_test_client.sh*, which allows you to run
the test client in full operation mode and test mode.

The run script supports three commands: 
- ```start <server hostname>```: start the client in full operation mode, connecting it to the given server.
- ```test <server hostname>```: start the client in test mode, connecting it to the given server.
- ```clean```: remove all logs written by the client.
For example, to start the client in full operation mode, connecitng it to a server on the local machine, use
```./coniks_test_client.sh start localhost```
Analogously to test the client, and remove the logs (takes no second argument).

Once running, the client prompts you to enter an operation, the number of users for which to
perform the operation and the first dummy user for which to run the operation. Dummy users are
identified by numbers, so user "5" is the 5th dummy user.
The test client will prompt you until you no longer want to continue.

Supported operations: 
- ```REGISTER```: register a new name-to-public key mapping with the CONIKS server.
- ```LOOKUP```: look up a public key, and verify the cryptographic proof of inclusion if the user exists.
- ```SIGNED```: change the public key registered for an existing name and authorize this change via a digital signature.
- ```UNSIGNED```: change the public key registered for an existing name, without authorization. This operation will fail if the affected user doesn't allow unsigned key changes.
` ```POLICY```: change the key change policy -- if unsigned changes are allowed, disallow them, and vice versa. The default policy is to allow unsigned changes.

Some examples:
- REGISTER 10 10: registers 10 new users, identified as dummy users 10 through 19.
- LOOKUP 1 18: looks up the key for dummy user 18.
- SIGNED 10 10: performs a signed key data change for users 10 through 19.
- UNSIGNED 10 10: performs an unsigned key data change for users 10 throught 19.
- POLICY 1 18: changes the key change policy for user 18.

## Test Client Installation on a Remote Machine
You may want to install the test client on a remote machine.
Set the ```PUBUSER```, ```PUBHOST``` and ```PUBPATH``` variables in the Makefile.
```PUBUSER``` will need ssh access to the remote machine.
You'll then have to run the setup steps on the remote machine or send the appropriate files.
Assuming you've built the test client locally, run:
```
make pubbin
```
Next, install the run script on the remote machine:
```
make pubscr
```
You may need to change the permissions on the script to be able to execute it on the remote machine.

## Disclaimer
Please keep in mind that this CONIKS reference implementation is under active development. The repository may contain experimental features that aren't fully tested. We recommend using a [tagged release](https://github.com/citp/coniks-ref-implementation/releases).

##Documentation
[Read the test client's Java API (javadoc)](https://coniks-sys.github.io/coniks-ref-implementation/org/coniks/coniks_test_client/package-summary.html)
