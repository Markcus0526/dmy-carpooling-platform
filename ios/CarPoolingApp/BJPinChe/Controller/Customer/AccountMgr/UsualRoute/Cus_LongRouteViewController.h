//
//  Cus_UsualRouteViewController.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-19.
//  Copyright (c) 2014年 KimOC. All rights reserved.
// 设置日常路线 未完成   不再开发

#import <UIKit/UIKit.h>
#import "LongRouteCell.h"

@interface Cus_LongRouteViewController : SuperViewController <UITableViewDataSource, UITableViewDelegate, AccountSvcDelegate, RMSwipeTableViewCellDelegate, RMSwipeTableViewCellDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UITableView *              _tableView;
    
    /********************** Member Variable ***********************/
    NSMutableArray *                    mRouteArray;
}

@property (nonatomic, strong) NSIndexPath *selectedIndexPath;

- (IBAction)onClickedBack:(id)sender;

@end
