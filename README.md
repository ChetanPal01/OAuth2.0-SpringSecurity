# OAuth-spring-security-tutorial

OAuth (Open Authorization) is a simple way to publish and interact with protected data.
It is an open standard for token-based authentication and authorization. It allows an end user's account information to be used by third-party services,
such as Facebook, without exposing the user's password.

### Example

Consider the use case of Quora or stack overflow. Go to Quora.com.
If you are a new user you need to signup. You can signup using google or facebook account. When doing so you are authorizing Google or 
Facebook to allow quora to access you profile info with Quora. This authorizing is done using OAuth. Here you have in no way shared your credentials with Quora.

In the above example of Quora, we have 4 actors-

#### Resource Owner - This is the user who wants to sign up using Quora.
#### Client Application - This will be Quora
#### Resource Server - This will be Gmail or Facebook.
#### Authorization Server - The resource server hosts the protected user accounts, and the authorization server verifies the identity of the user then issues access tokens to the application.

When using OAuth2, grant type is the way an application gets the access token. There are the 5 grant types according to OAuth2 specification-
1. Authorization code grant
2. Implicit grant
3. Resource owner credentials grant - (This is not a machine to machine authentication)
4. Client credentials grant - (Machine to machine authentication example Trivago making call to make my trip, booking.com etc to show the data)
Refresh token grant

![img_1.png](img_1.png)

### In this application :

we will be implementing our own client application and resource server.
The resource owner will then using OAuth authorize the resource server to share data with the client application.
The client application must first register with the authorization server associated with the resource server. This is usually a one-time task. Once registered, 
the registration remains valid, unless the client application registration is revoked. At registration the client application is assigned a client ID and a 
client secret (password) by the authorization server. The client ID and secret is unique to the client application on that authorization server.
