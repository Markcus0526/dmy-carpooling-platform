//
//  Cus_EvalDriverLatestViewController.m
//  BJPinChe
//
//  Created by yc on 14-11-26.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

//乘客获取评价信息

#import "Cus_EvalDriverLatestViewController.h"
#import "UIViewController+CWPopup.h"

@interface Cus_EvalDriverLatestViewController ()<UITextViewDelegate>

@end

@implementation Cus_EvalDriverLatestViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    _textView.delegate =self;
    _textView.editable = NO;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void) setParent : (id)parent message:(NSString *)message level:(int)level{
    mParent = parent;
    [_textView setText:message];
    switch (level) {
        case 1:
        {
            [_statBtn1 setBackgroundImage:[UIImage imageNamed:@"def_radio_active.png"] forState:UIControlStateNormal];
            [_statBtn2 setBackgroundImage:[UIImage imageNamed:@"def_radio_normal.png"] forState:UIControlStateNormal];
            [_statBtn3 setBackgroundImage:[UIImage imageNamed:@"def_radio_normal.png"] forState:UIControlStateNormal];
        }
            break;
        case 2:
        {
            [_statBtn1 setBackgroundImage:[UIImage imageNamed:@"def_radio_normal.png"] forState:UIControlStateNormal];
            [_statBtn2 setBackgroundImage:[UIImage imageNamed:@"def_radio_active.png"] forState:UIControlStateNormal];
            [_statBtn3 setBackgroundImage:[UIImage imageNamed:@"def_radio_normal.png"] forState:UIControlStateNormal];
        }
            break;
        case 3:
        {
            [_statBtn1 setBackgroundImage:[UIImage imageNamed:@"def_radio_normal.png"] forState:UIControlStateNormal];
            [_statBtn2 setBackgroundImage:[UIImage imageNamed:@"def_radio_normal.png"] forState:UIControlStateNormal];
            [_statBtn3 setBackgroundImage:[UIImage imageNamed:@"def_radio_active.png"] forState:UIControlStateNormal];
        }
            break;
            
        default:
            break;
    }
}
- (IBAction)cancelClick:(id)sender {
    UIViewController * parent = (UIViewController *)mParent;
    
    // dismiss popup
    if (parent.popupViewController != nil) {
        [parent dismissPopupViewControllerAnimated:YES completion:^{
            NSLog(@"popup view dismissed");
        }];
    }
}
@end
