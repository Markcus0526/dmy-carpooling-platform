//
//  AppMgrViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AppMgrViewController : UIViewController <AccountSvcDelegate, MainSvcDelegate, UIAlertViewDelegate>
{
    NSMutableArray *        mAllAppArray;
    NSMutableArray *        mAppArray1;
    NSMutableArray *        mAppArray2;
	
	int									anc_count;
	int									order_count;
	int									person_count;

	NSString*				versionURL;
}

@property (retain, nonatomic) IBOutlet UIScrollView *       ctrlScroller1;
@property (retain, nonatomic) IBOutlet UIScrollView *       ctrlScroller2;

@end
