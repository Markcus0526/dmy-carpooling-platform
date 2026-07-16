//
//  OurStoryViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_OurStoryViewController : SuperViewController

- (IBAction)onClickedBack:(id)sender;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UIView *brackView;
@property (weak, nonatomic) IBOutlet UILabel *lable;

@end
