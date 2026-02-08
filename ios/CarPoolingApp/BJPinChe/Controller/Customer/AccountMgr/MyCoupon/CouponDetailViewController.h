//
//  CouponDetailViewController.h
//  BJMainApp
//
//  Created by KimOC on 8/10/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CouponDetailViewController : SuperViewController
{
    IBOutlet UILabel *          _lblContents;
    IBOutlet UILabel *          _lblUnitname;
    IBOutlet UILabel *          _lblCode;
}

@property (nonatomic, retain) NSString *        mContents;
@property (nonatomic, retain) NSString *        mUnitname;
@property (nonatomic, retain) NSString *        mCode;

@end

