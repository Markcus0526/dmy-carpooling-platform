//
//  SureViewController.h
//  BJPinChe
//
//  Created by CKK on 14-9-17.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol SureViewControllerDelegate <NSObject>

-(void)SureViewResuslt:(BOOL)isYES;

@end

@interface SureViewController : UIViewController
@property (weak, nonatomic) IBOutlet UILabel *messageLabel;
- (IBAction)OnSureClick:(id)sender;
- (IBAction)OnCancelClick:(id)sender;
@property (assign, nonatomic) id<SureViewControllerDelegate> delegate;

@end

