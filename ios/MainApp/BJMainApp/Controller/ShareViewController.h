//
//  ShareViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/25/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UMSocial.h"

@interface ShareViewController : UIViewController <AccountSvcDelegate, UMSocialUIDelegate>
{
    
    __weak IBOutlet UIWebView *webView;
    
    /********************** Member Variable **********************/
    NSString *                      mContents1;
    NSString *                      mContents2;
    NSString *                      mContents3;
    
}

- (IBAction)onTapShare:(id)sender;

@end
