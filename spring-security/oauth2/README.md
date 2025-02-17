## Take Note
- dependencies
    - oauth client

- by just running the application and going to the default route, you are being required to login

- set the oauth2 client registration

```python

> create the client id for the app
  > google - go to google cloud
  > github - go to github developer settings

Authorised redirect URIs / authorization callback url
http://localhost:8080/login/oauth2/code/google
http://localhost:8080/login/oauth2/code/github



```