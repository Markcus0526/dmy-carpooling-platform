//
//  MyCouponViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PullToRefreshTable.h"
#import "Cus_MyCouponTableViewCell.h"

@interface Cus_MyCouponViewController : SuperViewController<PullToRefreshDelegate, UITableViewDataSource, UITableViewDelegate, TradeSvcDelegate, CouponCellDelegate, MainSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UITextField *          _txtSearch;
    
    // news table
    IBOutlet UIView *           _vwTableFrame;
    PullToRefreshTable *        _mainTable;
    
    /********************** Member Variable ***********************/
    NSMutableArray *            mCouponArray;
    
    int                         mCurPageNum;
    
}

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedAdd:(id)sender;

@end
