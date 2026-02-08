//
//  InsuranceDetailModal.m
//  BJPinChe
//
//  Created by APP_USER on 14-11-19.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "InsuranceDetailModal.h"
#import "STDataInfo.h"
#import "NSString+Extension.h"
@interface InsuranceDetailModal ()

@end

@implementation InsuranceDetailModal

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)loadView
{
    [super loadView];
    if (self.mOrderInfo.appl_no == nil) {
        NSString *appl_num =@"保险单号：无";
        [self.appl_num_Label setText:appl_num];
        
        [self.isd_name_Label setText:@"被保人姓名：无"];
        
        [self.effect_time_Label setText:@"保单生效时间：无"];
        [self.insexpr_date_Label setText:@"保单中止时间：无"];
        
        NSString *total_amount =[NSString stringWithFormat:@"%.1f点",self.mOrderInfo.total_amount];
        ;
        
        self.total_amount_Label.attributedText =[NSString attributedStringFromNString:total_amount];
    }else
    {
        NSString *appl_num =[NSString stringWithFormat:@"保险单号：%@",self.mOrderInfo.appl_no];
        [self.appl_num_Label setText:appl_num];
        
        [self.isd_name_Label setText:[NSString stringWithFormat:@"被保人姓名：%@",self.mOrderInfo.isd_name]];
        
        [self.effect_time_Label setText:[NSString stringWithFormat:@"保单生效时间：%@",self.mOrderInfo.effect_time]];
        [self.insexpr_date_Label setText:[NSString stringWithFormat:@"保单中止时间：%@",self.mOrderInfo.insexpr_date]];
        
        NSString *total_amount =[NSString stringWithFormat:@"%.1f点",self.mOrderInfo.total_amount];
        ;
        
        
        self.total_amount_Label.attributedText =[NSString attributedStringFromNString:total_amount];
    }
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onClickedDone:(id)sender {
   
    if([self.delegate respondsToSelector:@selector(tappedDone)])
    {
        [self.delegate tappedDone];
    }
    
}
@end
