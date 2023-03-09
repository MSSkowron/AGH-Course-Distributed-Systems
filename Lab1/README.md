# How to run

1. Compile Java files
   ```
   javac Server.java
   javac Client.java
   ```
2. Run Server
   ```
   java Server.java
   ```
3. Run Client
   ```
   java Client.java
   ```

# How to send messages

1. **Send normal message using TCP**
   Just type message content in the terminal.

2. **Send ASCII Art using UDP**
   Use _@U_ message type followed by a ASCII ART filepath.
   ```
   @U file.txt
   ```
3. **Send ASCII Art using Multicast**
   Use _@M_ message type followed by a ASCII ART filepath.
   ```
   @M file.txt
   ```
4. **Leave the Chat**
   Use _@EXIT_ message type.

   ```
   @EXIT
   ```
