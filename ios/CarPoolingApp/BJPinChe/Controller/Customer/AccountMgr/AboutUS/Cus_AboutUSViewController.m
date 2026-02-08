//
//  AboutUSViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Cus_AboutUSViewController.h"

@interface Cus_AboutUSViewController ()

@end

@implementation Cus_AboutUSViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    NSString *version = [[[NSBundle mainBundle] infoDictionary] objectForKey:(NSString *)kCFBundleVersionKey];
    _lblVersion.text =version;
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


///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation 

/**
 * Back to parent view controller
 */
- (IBAction)versionClick:(id)sender {
    [SVProgressHUD show];
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetLatestAppVersion:[Common getDeviceMacAddress]];
}

- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}

/**
 * Call company telephone
 */
- (IBAction)onClickedPhone:(id)sender;
{
    // init with phone number
    NSString * sPhoneNum = COMPLAIN_TEL_NUM;
    // call this phone number
    
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",sPhoneNum]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];

//    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@", sPhoneNum]]];
}
-(void)getLatestAppVersion:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        [SVProgressHUD dismiss];
        NSDictionary *dic = [dataList objectAtIndex:0];
        NSString *version  = [dic objectForKey:@"latestver"];
        versionURL = [dic objectForKey:@"downloadurl"];
        if ([version doubleValue]-[_lblVersion.text doubleValue]>0) {
            UIAlertView *alert =[[UIAlertView alloc]initWithTitle:@"提示!" message:@"有新的版本需要更新" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
            [alert show];
        }else
            [SVProgressHUD showSuccessWithStatus:@"已经是最新版本了" duration:DEF_DELAY];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
    {
        //[[UIApplication sharedApplication] openURL:[NSURL URLWithString:versionURL]];
        
        NSURL *iTunesURL = [NSURL URLWithString:versionURL];
        
        UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
        [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:iTunesURL]];
        [ self.view addSubview:phoneCallWebView];
        
    }
}
@end
