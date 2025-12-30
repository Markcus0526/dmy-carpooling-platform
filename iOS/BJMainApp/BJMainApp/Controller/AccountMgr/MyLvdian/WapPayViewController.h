//
//  WapPayViewController.h
//  BJPinChe
//
//  Created by KHM on 14-11-24.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WapPayViewController : UIViewController <UIWebViewDelegate, NSURLConnectionDelegate>
{
	__weak IBOutlet UIWebView *webContentView;
}

@property(nonatomic, retain) NSString* orderInfo;

@end
