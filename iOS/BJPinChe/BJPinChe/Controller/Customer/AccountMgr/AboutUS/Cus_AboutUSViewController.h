//
//  AboutUSViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Cus_AboutUSViewController : SuperViewController<UIAlertViewDelegate,AccountSvcDelegate>
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
