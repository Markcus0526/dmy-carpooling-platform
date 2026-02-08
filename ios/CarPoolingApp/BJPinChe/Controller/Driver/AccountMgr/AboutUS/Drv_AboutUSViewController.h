//
//  AboutUSViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_AboutUSViewController : SuperViewController<AccountSvcDelegate,UIAlertViewDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UIImageView *          _imgMain;
    IBOutlet UILabel *              _lblVersion;
    NSString *versionURL;
}
- (IBAction)versionClick:(id)sender;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedPhone:(id)sender;

@end
