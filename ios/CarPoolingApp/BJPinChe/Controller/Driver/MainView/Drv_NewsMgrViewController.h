//
//  NewsMgrViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/23/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Drv_NewsTableViewCell.h"

@interface Drv_NewsMgrViewController : SuperViewController<UITableViewDataSource, UITableViewDelegate, UIScrollViewDelegate, MainSvcDelegate, Drv_NewsCellDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UILabel *          _lblTitle1;
    IBOutlet UILabel *          _lblTitle2;
    IBOutlet UILabel *          _lblTitle3;
    IBOutlet UILabel *          _ctrlIndicator;
    
    IBOutlet UIScrollView *		_MainScrollView;
    
    
    IBOutlet UIView *           _vwNewsView1;
    IBOutlet UIView *           _vwNewsView2;
    IBOutlet UIView *           _vwNewsView3;
    
    // news table
    IBOutlet UIView *           _vwNewsFrame1;
    IBOutlet UIView *           _vwNewsFrame2;
    IBOutlet UIView *           _vwNewsFrame3;
    UITableView *               _newsTable1;
    UITableView *               _newsTable2;
    UITableView *               _newsTable3;
    
    /********************** Member Variable ***********************/
    NSMutableArray *            mNewsArray1;
    NSMutableArray *            mNewsArray2;
    NSMutableArray *            mNewsArray3;
    
    int                         mCurPageNum1;
    int                         mCurPageNum2;
    int                         mCurPageNum3;
    
    int                         mCurTab;
}

- (IBAction)onClickedBack:(id)sender;

- (IBAction)onClickedTab1:(id)sender;
- (IBAction)onClickedTab2:(id)sender;
- (IBAction)onClickedTab3:(id)sender;

@end
