//
//  MainTabViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MainTabViewController : UITabBarController <UITabBarControllerDelegate, AccountSvcDelegate>

@property (nonatomic, readwrite) NSInteger mCurTab;

@end
