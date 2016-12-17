#!/usr/bin/python
import config
import dropbox

def get_flow():
    return dropbox.client.DropboxOAuth2FlowNoRedirect(config.app_key, config.app_secret)

def get_client():

    flow = get_flow()
    access_token, user_id = oauth(flow)
    print(access_token, user_id)

    client = dropbox.client.DropboxClient(access_token)
    print 'linked account: ', client.account_info()

    return client

def oauth(flow):

    authorize_url = flow.start()
    print '1. Go to: ' + authorize_url
    print '2. Click "Allow" (you might have to log in first)'
    print '3. Copy the authorization code.'
    code = raw_input("Enter the authorization code here: ").strip()
    return flow.finish(code)

def upload_dummy(client):
    f = open('dummy.txt', 'rb')
    response = client.put_file('/dummy.txt', f)
    print "uploaded:", response

def download_dummy(client):
    f, metadata = client.get_file_and_metadata('/dummy.txt')
    out = open('dummy-copied.txt', 'wb')
    out.write(f.read())
    out.close()
    print metadata

if __name__ == "__main__":

    # init
    client = get_client()

    # upload
    upload_dummy(client)

    # download
    download_dummy(client)
