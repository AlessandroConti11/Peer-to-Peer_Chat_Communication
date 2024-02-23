# Peer-to-Peer Chat Communication


Author: Alessandro Conti - [AlessandroConti11](https://github.com/AlessandroConti11)

License: [MIT license](LICENSE).

Tags: `#chat`, `#java`, `#P2P`, `#peer-to-peer`, `#software_engineering`.


## Specification

A simplified implementation of a multicast chat can be found in this repository.

Steps to follow to chat with other peers:
1. register as a peer
   ```
   Insert username and port number for this peer in the following format "username port_number": 
   ```
2. connect with other peers
   ```
   To connect to other peers insert their ip and port number in the following format "PeerIP:PortNumber ...": 
   ```
3. send message in multicast
    ```
   Now you can communicate with other peers (e to exit, c to change) 
   - Insert "exit" to quit from the chat 
   - Insert "change" to change the group chat 
   ```
   

## Final Consideration

This implementation is very simplified; in fact, many controls are not executed; to make it work best, it is important not to make mistakes when entering commands.
