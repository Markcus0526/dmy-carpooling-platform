//
//  Cus_LongOrderPasswordViewController.h
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>


// service protocol
@protocol pwdPopupDelegate <NSObject>

@optional

- (void) tappedDone : (NSString *)result;

@end


@interface PasswordCheckModal : UIViewController

@property (weak, nonatomic) IBOutlet UILabel *ShowLabel;
@property (weak, nonatomic) IBOutlet UILabel *tipsLabel;

- (IBAction)OnNumClick:(id)sender;
- (IBAction)OnClearClick:(id)sender;
- (IBAction)OnDoneClick:(id)sender;

@property(nonatomic,copy)NSString *tips;

@property(strong, nonatomic) id<pwdPopupDelegate> delegate;

@end




