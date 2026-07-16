//
//  AccumViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PullToRefreshTable.h"

@interface Cus_AccumViewController : SuperViewController<PullToRefreshDelegate, UITableViewDataSource, UITableViewDelegate, TradeSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UILabel *          _lblLeftAccum;
    
    // news table
    IBOutlet UIView *           _vwTableFrame;
    PullToRefreshTable *        _mainTable;
    
    /********************** Member Variable ***********************/
    NSMutableArray *            mAccumHisArray;
    double                      mLeftMoney;
    
    int                         mCurPageNum;
}
@property(nonatomic,assign) BOOL RefreshOrNot;

- (IBAction)onClickedBack:(id)sender;

- (IBAction)onClickedMenuCharge:(id)sender;
- (IBAction)onClickedMenuWithdraw:(id)sender;

@end
