//
//  InsuranceModal.m
//  BJPinChe
//
//  Created by APP_USER on 14-11-19.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "InsuranceModal.h"
#import "STDataInfo.h"
#import "NSString+Extension.h"
@interface InsuranceModal ()

@end

@implementation InsuranceModal

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
    
    NSString *systemfee =[NSString stringWithFormat:@"%.1f点",self.morderInfo.sysinfo_fee];
    self.systemFeeLabel.attributedText =[NSString attributedStringFromNString:systemfee];
    
    NSString *insuranceFee =[NSString stringWithFormat:@"%.1f点",self.morderInfo.insun_fee];
    self.insuranceFeeLabel.attributedText =[NSString attributedStringFromNString:insuranceFee];
    
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)onClickedDone:(id)sender {
    [self.delegate tappedDone];
    
    //[self dismissViewControllerAnimated:YES completion:nil];
}
@end
