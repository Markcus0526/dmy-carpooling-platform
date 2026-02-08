//
//  Cus_EvalDriverLatestViewController.h
//  BJPinChe
//
//  Created by yc on 14-11-26.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Cus_EvalDriverLatestViewController : UIViewController
{
    id                              mParent;
}
@property (weak, nonatomic) IBOutlet UIButton *statBtn1;
- (IBAction)cancelClick:(id)sender;

@property (weak, nonatomic) IBOutlet UIButton *statBtn2;
@property (weak, nonatomic) IBOutlet UIButton *statBtn3;

- (void) setParent : (id)parent message:(NSString *)message level:(int)level;
@property (weak, nonatomic) IBOutlet UITextView *textView;

@end
