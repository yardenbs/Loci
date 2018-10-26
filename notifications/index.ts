const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

// Listens for new comment added to /comments/:pushId and sends a push notification
exports.sendCommentNotification = functions.database
.ref('/Comments/{postId}/{commentId}')
.onCreate((snapshot, context) => {

    const comment =snapshot.val();
    admin.database().ref(`/Users/${comment.mUid}`).once("value", (snapshot2) => {

        const user = snapshot2.val();

                    // Notification details.
                    const payload = {
                        notification: {
                            title: 'You have a new comment!',
                            body: `${comment.mCreator} commented on your post.`
                        }
                    };
                    const token = user.mToken;
                    return admin.messaging().sendToDevice(token, payload);
            

             });
        });

