# How to Use Tensai CMS

**Welcome to Tensai CMS!**

This platform transforms your Telegram group into a powerful content management system. By simply chatting in Telegram
topics, you can draft, format, and publish articles directly to your blog.

Here is your step-by-step guide to getting started.

---

## Phase 1: The Initial Setup

*You only need to do this once.*

1. **Create an empty Telegram Group.**
2. **Enable Topics:** Go to the group's settings and toggle on **Topics** (this is required to separate your different
   blog posts).
3. **Add the Bot:** Add [@tensai_cms_bot](https://t.me/tensai_cms_bot) to your group.
4. **Grant Admin Permissions:** Promote the bot to an Admin. **Crucial:** Ensure the bot has the permission to **Delete
   Messages**.

The first time you add [@tensai_cms_bot](https://t.me/tensai_cms_bot) to a group as an admin it will create you an
account with your telegram username *(if not set the bot will generate you a username)* and send you a reset password
URL, the next times you add [@tensai_cms_bot](https://t.me/tensai_cms_bot) to different groups it'll manage only the new
group without deleting old blogs.

> *[@tensai_cms_bot](https://t.me/tensai_cms_bot) currently can only manage one group for each user*

> **Collaborative working :**[@tensai_cms_bot](https://t.me/tensai_cms_bot) currently only store the user who promoted
> it to an admin as the owner of the blog and doesn't track if different users contributed to the blog.

---

## Phase 2: Creating & Publishing a Blog

### 1. Start a New Draft

To create a new blog post, simply **create a new topic** in your group.

* The **Topic Name** becomes your **Blog Title**.
* Want to change the title later? Just rename the topic in Telegram, and the blog title will update after the next
  publish!

### 2. Add Content

Write naturally in the topic. The CMS supports a rich variety of media:

* Texts
* Images and Videos
* Audio and Voice Notes
* Documents and Files

> **💡 Pro-Tip for Images:** To preserve the highest quality, send your images as a **File** rather than a standard photo
> upload. Telegram compresses standard photos, but files remain untouched.

### 3. Set a Summary

Give your readers a preview of your article.

* Send `/setsummary <your summary text>` to define the excerpt.
* *Example:* `/setsummary A deep dive into Java 25 features.`
* If you make a mistake, use `/clearsummary` to remove it.

### 4. Publish

When your draft is ready, simply send `/publish` in the topic. Your thread is instantly converted into a live,
structured article!

---

## Phase 3: Deleting & Editing

### Deleting Specific Messages

If you make a typo or want to remove a message, you **must** use the bot to delete it from the CMS.

- Reply directly to the message you want to remove with `/delete` and hit send.

> **⚠️ WARNING:** Do not use Telegram's native "Delete message" option. If you delete a message natively, the bot won't
> know it happened, and the text will remain in your published blog. Always use `/delete`!

### Editing Specific Messages

If you make a typo or want to change a message (including media), you can use Telegram's native "Edit message" option.

### Deleting an Entire Blog

To delete a blog entirely (which removes the published version, the draft, and the Telegram topic):

1. Send `/settings` in the topic.
2. Click the **Delete Blog** inline button.
3. Click *"Are you sure?"* to confirm.

---

## Phase 4: Managing your Account

### Reset account password

To reset your account password:

1. Send `/settings` in your private chat with [@tensai_cms_bot](https://t.me/tensai_cms_bot).
2. Click the **Reset Password** inline button.
3. It will generate a secure password reset link valid for 15 minutes.

## Command Reference

### Commands Inside a Topic (Managing a Blog)

* `/publish` - Publishes the current topic as a live blog post.


* `/setsummary <text>` - Sets the preview summary of the blog.


* `/clearsummary` - Removes the current summary.


* `/delete` - Deletes a specific block of content (You **must** reply to the message you want to delete).


* `/settings` - Opens topic settings.


* `/help` - Displays a list of the available topic commands.

### Commands in Private Chat (Account Management)

Send these directly in a private message to [@tensai_cms_bot](https://t.me/tensai_cms_bot):

* `/start` - Starts a conversation and sends the link to this tutorial.


* `/settings` - Opens your account settings.