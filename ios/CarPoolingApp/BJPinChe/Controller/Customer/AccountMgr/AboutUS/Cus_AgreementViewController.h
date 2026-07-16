//
//  Cus_AgreementViewController.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-20.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Cus_AgreementViewController : SuperViewController

@property(nonatomic, weak) IBOutlet UIScrollView*		scrollView;
@property(nonatomic, weak) IBOutlet UIView*				backView;
@property(nonatomic, weak) IBOutlet UILabel*			lblContent;

- (IBAction)onClickedBack:(id)sender;

@end
