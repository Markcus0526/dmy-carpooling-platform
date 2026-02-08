//
//  AccountMgrViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AccountMgrViewController : UIViewController <AccountSvcDelegate, UIAlertViewDelegate>

- (IBAction)onClickedLogout:(id)sender;

@end
