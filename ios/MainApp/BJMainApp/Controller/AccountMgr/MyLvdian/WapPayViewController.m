//
//  WapPayViewController.m
//  BJPinChe
//
//  Created by KHM on 14-11-24.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "WapPayViewController.h"

@interface WapPayViewController ()

@end


#define WAP_BASE_URL @"https://www.baifubao.com/api/0/pay/0/wapdirect?"


@implementation WapPayViewController

@synthesize orderInfo;

- (void)viewDidLoad {
    [super viewDidLoad];

	// Do any additional setup after loading the view from its nib.
	NSString* szUrl = [WAP_BASE_URL stringByAppendingString:orderInfo];
	webContentView.scalesPageToFit = YES;
	webContentView.delegate = self;

	[webContentView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:szUrl]]];

	NSLog(@"\n\nUrl : %@\n", szUrl);
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
- (IBAction)onClickBack:(id)sender {
	UINavigationController* navController = self.navigationController;
	if (navController == nil)
	{
		[self dismissViewControllerAnimated:YES completion:nil];
	}
	else
	{
		[navController popViewControllerAnimated:YES];
	}
}


- (void)webViewDidStartLoad:(UIWebView *)webView
{
	[SVProgressHUD showWithStatus:@"请稍后..." maskType:SVProgressHUDMaskTypeClear];
}



- (void)webViewDidFinishLoad:(UIWebView *)webView
{
	[SVProgressHUD dismiss];
}



- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
	if ([error.domain isEqualToString:@"NSURLErrorDomain"] && error.code == NSURLErrorCancelled)
	{
		return;
	}

	[SVProgressHUD dismiss];
	NSLog(@"Error:%@", error.description);
}



@end
