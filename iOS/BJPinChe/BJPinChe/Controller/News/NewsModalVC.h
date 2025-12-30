//
//  NewsModalVC.h
//  BJPinChe
//
//  Created by APP_USER on 14-10-15.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "STDataInfo.h"
@interface NewsModalVC : UIViewController

@property(nonatomic,strong)STNewsInfo *newsInfo;
@property (weak, nonatomic) IBOutlet UITextView *textView;

@end
